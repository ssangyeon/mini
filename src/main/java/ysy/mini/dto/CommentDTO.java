package ysy.mini.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ysy.mini.entity.CommentEntity;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); //Service 메서드에 @Transactional
        return commentDTO;
    }
}
