<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file= "../layout/header.jsp" %>
<div class="container">

    <!-- 수정/삭제 버튼 생성 -->
    <button class="btn btn-grey" onclick="history.back()">돌아가기</button>
    <c:if test="${board.user.id == principal.user.id}">
        <a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
        <button id="btn-delete" class="btn btn-danger">삭제</button>
    </c:if>
    <hr />

    <!-- 글번호 -->
    <div>
        글번호:<span id="id"><i>${board.id} </i></span>
        작성자:<span><i>${board.user.username} </i></span>
    </div>
    <br /><br />


    <!-- 글제목 -->
    <div class="form-group">
        <h3>${board.title}</h3>
    </div>
    <hr />
    <!-- 글내용 -->
    <div class="form-group">
       <div>${board.content}</div>
    </div>
    <hr />
</div>
<script src="/js/board.js"></script>
<%@ include file = "../layout/footer.jsp" %>