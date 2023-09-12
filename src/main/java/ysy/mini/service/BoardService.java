package ysy.mini.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ysy.mini.dto.BoardDTO;
import ysy.mini.entity.BoardEntity;
import ysy.mini.entity.BoardFileEntity;
import ysy.mini.repository.BoardFileRepository;
import ysy.mini.repository.BoardRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//DTO -> Entity (Entity Class)
//Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        //파일 첨부 여부에 따라 로직 분리
        if(boardDTO.getBoardFile().stream().findFirst().get().isEmpty() ) {

            //첨부 파일 없음.
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        }else {
            //첨부 파일 있음.
            /*
            1.DTO에 담긴 파일을 꺼냄
            2.파일의 이름 가져옴
            3.서버 저장용 이름을 만듦
            //내사진.jpg => 834258932475983_내사진.jpg
            4.저장 경로 설정
            5.해당 경로에 파일 저장
            6.board_table에 해당 데이터 save 처리
            7.board_file_table에 해당 데이터 save 처리
             */
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO); //여기에는 id값이 없기때문에 db에서 entity를 얻어와야함
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();//여기에 id값이 있다.
            for(MultipartFile boardFile: boardDTO.getBoardFile()){
//            MultipartFile boardFile = boardDTO.getBoardFile(); //1.
            String originalFilename = boardFile.getOriginalFilename(); //2.
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; //3.
            String savePath = "/Users/sangyeon/sudy/springboot_img/"+storedFileName; //4.
            boardFile.transferTo(new File(savePath)); //5.
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);}
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional //수동적인 쿼리를 수행하는경우 사용
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional //toBoardDTO 부모엔티티에서 자식엔티티를 접근할때 그내용을 호출하는 메서드에서 boardentity가 boardfileentity에 접근시 사용
    public BoardDTO findById(Long id){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

//    @Autowired
//    private EntityManager entityManager;
//    @Transactional
//    public BoardDTO update(BoardDTO boardDTO) {
//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDTO.getId());
//        if (optionalBoardEntity.isPresent()) {
//            BoardEntity existingBoardEntity = optionalBoardEntity.get();
//
//            // 기존 엔터티를 수정
//            existingBoardEntity.setBoardWriter(boardDTO.getBoardWriter());
//            existingBoardEntity.setBoardPass(boardDTO.getBoardPass());
//            existingBoardEntity.setBoardTitle(boardDTO.getBoardTitle());
//            existingBoardEntity.setBoardContents(boardDTO.getBoardContents());
//            existingBoardEntity.setBoardHits(boardDTO.getBoardHits());
//            existingBoardEntity.setFileAttached(boardDTO.getFileAttached());
//
//            // 업데이트된 엔터티 저장
//            boardRepository.save(existingBoardEntity);
//
//            entityManager.flush();
//
//            // 업데이트된 엔터티를 반환
//            return findById(existingBoardEntity.getId());
//        } else {
//            // 엔터티가 존재하지 않을 경우 예외 처리 또는 다른 방법 사용
//            return null;
//        }
//    }
//    아래방식의 toUpdateEntity는 새로운 객체를 생성하여 createdtime필드를 초기화하지 않아서 글수정 버튼을 눌렀을때 createdtime 부분이 null로나옴
//    update 할떄 id값이 원래 테이블에 있는 값이면 업데이트로 인식한다는데 새로운 객체를 생성해서 createdtime이 null로 나오는건가?
    @Transactional
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);//id가 있으면 업데이트하라는걸로 알아들음
//        entityManager.flush();
        return findById(boardEntity.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        int pageLimit =3; //한페이지에 보여줄 글 갯수
        //한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        //page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(),board.getBoardWriter(),
                board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime()));
        return boardDTOS;
    }
}
