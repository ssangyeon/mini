package ysy.mini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysy.mini.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {
}
