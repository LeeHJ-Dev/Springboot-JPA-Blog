<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file= "../layout/header.jsp" %>
<div class="container">
    <form>
        <!-- 회원ID -->
        <input type="hidden" id="id" value="${principal.user.id}"/>

        <!-- 회원이름 -->
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" placeholder="Enter Username" id="username" value="${principal.user.username}" readonly>
        </div>

        <c:if test = "${empty principal.user.oauth}">
            <!-- 패스워드 -->
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" placeholder="Enter password" id="password">
            </div>
        </c:if>



        <!-- 이메일 -->
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" placeholder="Enter email" id="email" value="${principal.user.email}">
        </div>
    </form>
    <button id="btn-update" class="btn btn-primary">회원수정완료</button>
</div>
<!--주소 / 치면 -->
<script src="/js/user.js"></script>
<%@ include file = "../layout/footer.jsp" %>