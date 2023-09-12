package ysy.mini.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ysy.mini.dto.CommentDTO;
import ysy.mini.service.CommentService;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/save")
    private ResponseEntity  save(@ModelAttribute CommentDTO commentDTO){ // ajax 요청이므로 @ResponseBody 사용
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO);
        if(saveResult != null){
            //작성 성공하면 댓글목록을 가져와서 리턴
            //댓글목록: 해당 게시글의 댓글 전체
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity(commentDTOList, HttpStatus.OK);
        }else{
            return new ResponseEntity("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
