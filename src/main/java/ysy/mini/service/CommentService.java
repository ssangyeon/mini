package ysy.mini.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ysy.mini.dto.CommentDTO;
import ysy.mini.entity.BoardEntity;
import ysy.mini.entity.CommentEntity;
import ysy.mini.repository.BoardRepository;
import ysy.mini.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
        /*
        부모 엔티티(BoardEntity) 조회
         */
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO,boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else{
            return null;
        }
    }

    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /*
        EntityList -> DTOList
         */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity: commentEntityList){
           CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
           commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
