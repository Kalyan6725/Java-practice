package org.example.librarymgmt.repository;

import java.util.List;
import java.util.Optional;
import org.example.librarymgmt.dto.BranchFineTotalDTO;
import org.example.librarymgmt.dto.MemberSummaryDTO;
import org.example.librarymgmt.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findByMembershipBranch(String branch);

    @Query("SELECT ir.member FROM IssueRecord ir GROUP BY ir.member HAVING COUNT(ir) > :targetCount")
    List<Member> findAvidReaders(@Param("targetCount") long targetCount);

    @Query("""
            SELECT new org.example.librarymgmt.dto.BranchFineTotalDTO(
                m.membershipBranch,
                COALESCE(SUM(ft.amount), 0)
            )
            FROM Member m
            JOIN m.issueRecords ir
            JOIN ir.book b
            LEFT JOIN b.fineTransactions ft
            GROUP BY m.membershipBranch
            """)
    List<BranchFineTotalDTO> findTotalFinesPaidPerBranch();

    @Query("""
            SELECT ir.member
            FROM IssueRecord ir
            GROUP BY ir.member
            HAVING COUNT(DISTINCT ir.book.bookType) >= :minDistinctTypes
            """)
    List<Member> findMembersHoldingMultiGenreBookTypes(@Param("minDistinctTypes") long minDistinctTypes);

    @Query("""
            SELECT new org.example.librarymgmt.dto.MemberSummaryDTO(
                m.memberName,
                m.membershipBranch,
                COUNT(DISTINCT ir.issueId),
                COALESCE(SUM(ft.amount), 0)
            )
            FROM Member m
            LEFT JOIN m.issueRecords ir
            LEFT JOIN ir.book b
            LEFT JOIN b.fineTransactions ft
            GROUP BY m.memberId, m.memberName, m.membershipBranch
            """)
    List<MemberSummaryDTO> findMemberSummaries();

    @Query("""
            SELECT m.membershipBranch
            FROM Member m
            JOIN m.issueRecords ir
            JOIN ir.book b
            LEFT JOIN b.fineTransactions ft
            GROUP BY m.membershipBranch
            ORDER BY COALESCE(SUM(ft.amount), 0) DESC
            """)
    List<String> findTopBranchByFineCollection(Pageable pageable);

    @Query("""
            SELECT m.memberName
            FROM Member m
            JOIN m.issueRecords ir
            JOIN ir.book b
            LEFT JOIN b.fineTransactions ft
            GROUP BY m.memberId, m.memberName
            ORDER BY COALESCE(SUM(ft.amount), 0) DESC
            """)
    List<String> findHighestFinePayingMember(Pageable pageable);

    @Query("SELECT m.membershipBranch FROM Member m GROUP BY m.membershipBranch ORDER BY COUNT(m) DESC")
    List<String> findTopBranchByMemberCount(Pageable pageable);
}

