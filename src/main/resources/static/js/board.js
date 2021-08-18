let index = {
    init: function(){
        $("#btn-save").on("click",()=>{
            this.save();
        });

        $("#btn-delete").on("click",()=>{
            this.deleteById();
        });

        $("#btn-update").on("click",()=>{
            this.update();
        });

        $("#btn-reply-save").on("click",()=>{
            this.replySave();
        });
    },
    save: function(){
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };
        //-----------------------------------------------------------------------//
        // ajax 호출시 default 가 비동기 호출
        // ajax 통신을 이용해서 3개의 파라미터를 json 으로 변경하여 insert  요청.
        //-----------------------------------------------------------------------//
        $.ajax({
            //회원가입 수행 요청
            type: "POST",
            url: "/api/board",
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body 데이터가 어떤 타입인지(mime)
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
    },

    deleteById: function(){
        let id = $("#id").text();

        $.ajax({
            //회원가입 수행 요청
            type: "DELETE",
            url: "/api/board/" + id,
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            //성공
            alert("글삭제가 완료되었습니다.");
            //console.log(resp);
            location.href = "/";
        }).fail(function(error){
            //실패
            alert(JSON.stringify(error));
        });
    },
    //--------------------------------------------------------//
    //게시글수정
    //--------------------------------------------------------//
    update: function(){

        let id = $("#id").val();

        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        $.ajax({
            //회원가입 수행 요청
            type: "PUT",
            url: "/api/board/" + id,
            data:JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8",     //body 데이터가 어떤 타입인지(mime)
            dataType: "json" //요청을 서버로해서 응답이 왔을 때
        }).done(function(resp){
            //성공
            alert("글수정이 완료되었습니다. id : " + id);
            //console.log(resp);
            location.href = "/";
        }).fail(function(error){
            //실패
            alert(JSON.stringify(error));
        });
    },

    replySave: function(){
        let boardId = $("#boardId").val();
        //console.log(boardId);

        let data = {
             userId: $("#userId").val(),
             boardId: $("#boardId").val(),
             content: $("#reply-content").val()
        };
         //-----------------------------------------------------------------------//
         // ajax 호출시 default 가 비동기 호출
         // ajax 통신을 이용해서 3개의 파라미터를 json 으로 변경하여 insert  요청.
         //-----------------------------------------------------------------------//
         $.ajax({
             //댓글등록 수행 요청
             type: "POST",
             //url: "/api/board/" + boardId + "/reply",
             url: '/api/board/&{data.boardId}/reply',
             data:JSON.stringify(data), //http body 데이터
             contentType: "application/json; charset=utf-8",     //body 데이터가 어떤 타입인지(mime)
             dataType: "json" //요청을 서버로해서 응답이 왔을 때
         }).done(function(resp){
             //성공
             alert("댓글등록이 완료되었습니다.");

             location.href = "/board/" + $("#boardId").val();
         }).fail(function(error){
             //실패
             alert(JSON.stringify(error));
         });
     },

      replyDelete: function(boardId, replyId){
           $.ajax({
               //댓글등록 수행 요청
               type: "DELETE",
               url: "/api/board/${boardId}/reply/${replyId}",
               data:JSON.stringify(data), //http body 데이터
               contentType: "application/json; charset=utf-8",     //body 데이터가 어떤 타입인지(mime)
               dataType: "json" //요청을 서버로해서 응답이 왔을 때
           }).done(function(resp){
               if(resp.status == 500){
                    alert("댓글삭제에 실패하였습니다.");
               }else{
                    alert("댓글등록이 완료되었습니다.");
                    location.href = "/board/${boardId}";
               }
           }).fail(function(error){
               //실패
               alert(JSON.stringify(error));
           });
       }
}

index.init();