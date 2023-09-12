package ysy.mini.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ysy.mini.entity.BoardEntity;
import ysy.mini.repository.BoardRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardControllerTest {

    private static BoardRepository boardRepository;
    EntityManager em;

    @Test
    public void save() {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle("dd");
        System.out.println(boardEntity.getCreatedTime());

        em.persist(boardEntity);
        System.out.println(boardEntity.getCreatedTime());
        em.flush();
    }

}