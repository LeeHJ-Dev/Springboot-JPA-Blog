let index = {
    init: function(){
        $("#btn-save").on("click",()=>{
            this.save();
        });
        /*
        $("#btn-login").on("click",()=>{
            this.login();
        });
        */
    },
    save: function(){
        let data = {
            title: $("#title").val(),
            content: $("#content").val(),
        };
        //-----------------------------------------------------------------------//
        //ajax 호출시 default가 비동기 호출
        // ajax 통신을 이용해서 3개의 파라미터를 json 으로 변경하여 insert  요청.
        //-----------------------------------------------------------------------//
        $.ajax({
            //회원가입 수행 요청
            type: "POST",
            url: "/api/board",
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body데이터가 어떤 타입인지(mime)
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            //성공
            alert("글쓰기가 완료되었습니다.");
            //console.log(resp);
            location.href = "/";
        }).fail(function(error){
            //실패
            alert(JSON.stringify(error));
        });
    }
    /*
    ,
    login: function(){
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
        };

        $.ajax({
            //회원가입 수행 요청
            type: "POST",
            url: "/api/user/login",
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body데이터가 어떤 타입인지(mime)
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            //성공
            alert("로그인이 완료되었습니다.");
            location.href = "/";
        }).fail(function(error){
            //실패
            alert(JSON.stringify(error));
        });
    }
    */
}

index.init();