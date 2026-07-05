package org.example.librarymgmt.repository;

import java.util.List;
import org.example.librarymgmt.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    List<IssueRecord> findByMemberMemberId(Long memberId);
}

