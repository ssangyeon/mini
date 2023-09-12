package ysy.mini.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ysy.mini.dto.MemberDTO;
import ysy.mini.entity.MemberEntity;
import ysy.mini.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        //1.dto->entity로 변환
        //2.repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
        1. 회원이 입력한 이메일로 DB에서 조회를 함
        2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()){
            //조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다.)
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){ //string값 비교할떄 ==쓰면 안됨
                //비밀번호 일치
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else{
                //비밀번호 불일치(로그인 실패)
                return null;
            }
        }else{
            //조회 결과가 없다(해당 이메일을 가진 회원이 없다.)
            return null;
        }

}

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isPresent()){
            //조회결과가 있다-> 사용할수 없다
            return null;
        }else {
            //조회결과가 없다-> 사용할수 있다
            return "ok";
        }
    }
}
