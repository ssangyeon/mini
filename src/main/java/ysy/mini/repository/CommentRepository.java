package ysy.mini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysy.mini.entity.BoardEntity;
import ysy.mini.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // select * from comment_table where board_id=? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
