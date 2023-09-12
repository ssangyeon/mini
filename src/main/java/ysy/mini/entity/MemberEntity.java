package ysy.mini.entity;

import lombok.Getter;
import lombok.Setter;
import ysy.mini.dto.MemberDTO;

import javax.persistence.*;

@Entity
@Setter @Getter
@Table(name="member_table")
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql에서 auto_increment
    private Long id;

    @Column(unique = true) //unique 제약조건 추가
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        return memberEntity;
    }
    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        return memberEntity;
    }
}
