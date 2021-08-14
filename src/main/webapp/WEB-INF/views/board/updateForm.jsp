<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file= "../layout/header.jsp" %>
<div class="container">

    <form>
        <!-- 글번호 -->
        <input type="hidden" id="id" value="${board.id}"/>
        <!-- 글제목 -->
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" class="form-control" placeholder="Enter Title" id="title" value = "${board.title}">
        </div>

        <!-- 글내용 -->
        <div class="form-group">
          <label for="content">Content</label>
          <textarea class="form-control summernote" rows="5" id="content">${board.content}</textarea>
        </div>
    </form>

    <button id="btn-update" class="btn btn-primary">글수정완료</button>
</div>

<script>
    $('.summernote').summernote({
        placeholder: 'Hello Bootstrap 4',
        tabsize: 2,
        height: 300
    });
</script>
<script src="/js/board.js"></script>
<%@ include file = "../layout/footer.jsp" %>