let index = {
    init: function(){
        $("#btn-save").on("click",()=>{
            this.save();
        });
        $("#btn-update").on("click",()=>{
            this.update();
        });
    },
    save: function(){
        //alert("save function");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };
        //-----------------------------------------------------------------------//
        //ajax 호출시 default가 비동기 호출
        // ajax 통신을 이용해서 3개의 파라미터를 json 으로 변경하여 insert  요청.
        //-----------------------------------------------------------------------//
        $.ajax({
            //회원가입 수행 요청
            type: "POST",
            url: "/auth/joinProc",
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body데이터가 어떤 타입인지(mime)
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            if(resp.status == 500){
                alert("회원가입이 완료되었습니다.");
            }else{
                alert("회원가입이 완료되었습니다.");
                location.href = "/";
            }
        }).fail(function(error){
            //실패
            alert(JSON.stringify(error));
        });
    },
    update: function(){
        //let id = $("#id").val();
        console.log(id);
        let data = {
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };
        $.ajax({
            type: "PUT",
            url: "/user/",
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body데이터가 어떤 타입인지(mime)
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            //성공
            alert("회원수정이 완료되었습니다.");
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