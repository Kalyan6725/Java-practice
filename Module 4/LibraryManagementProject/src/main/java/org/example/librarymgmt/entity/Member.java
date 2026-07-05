package org.example.librarymgmt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    private String memberName;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 80)
    private String membershipBranch;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    @Builder.Default
    private String role = "USER";

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IssueRecord> issueRecords = new ArrayList<>();
}

