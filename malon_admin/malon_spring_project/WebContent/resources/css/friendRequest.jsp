<%@ page language="java" contentType="text/html; charset=EUC-KR"
   pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>       
<!-- Link Swiper's CSS -->
<link rel="stylesheet" href="/resources/css/add_friend.css">


<div id="request_freind">
	<h2><b>模备 夸没</b></h2>
	<nav id="friend_sub_menu_bar">
				<div id="mask">
					<ul id="list" style="-webkit-padding-start:0px;">
						<li><a href="/addfriend/addedfriend" id="friendList" data-hover="模备 格废">模备 格废</a></li>
						<li><a href="/addfriend/requestfriend" id="friendSuggestList" data-hover="模备 夸没">模备 夸没</a></li>
					</ul>
				</div>
			</nav>
	</div>
	<h4>模备夸没 格废</h4>
	<form id="reqFriendFrm">
		<table style="width: 30%">
			<colgroup>
				<col style="width: 44px;">
			</colgroup>
			<tbody>
				<tr>
					<td colspan="2" class="friendData">
						<input type="checkbox" onclick="allChecked(this)" >葛滴急琶<br/>
						<c:forEach var="e" items="${waitFriend }">
							<input type="checkbox" class="friend_chk" name="friends" value="${e.userId}">${e.userId}<br/>
						</c:forEach>
						<div id="Btn" style="text-align: center;">
							<input type="button"  id="acceptBtn" value="荐遏" onclick="acceptFriend()">
							<input type="button" id="refuseBtn" value="芭例" onclick="refuseFriend()">
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" id="friendArr" name="friendArr">
	</form>


<script type="text/javascript">
//模备 荐遏
function acceptFriend(){
 var friendArr = [];
 
	$("input:checkbox[name=friends]:checked").each(function(){
		friendArr.push($(this).val());
		});

	if(friendArr == ""){
		   alert("荐遏且 亲格阑 急琶秦林技夸.");
		   return false;
		} else{
		    $("#friendArr").attr('value', friendArr); 
			$('#reqFriendFrm').attr('action','/addfriend/acceptfriend');
		 	$('#reqFriendFrm').submit();
		}
  
}

function refuseFriend(){
	var boardIdxArray = [];
	
	$("input:checkbox[name=friends]:checked").each(function(){
	   boardIdxArray.push($(this).val());
	});
	
	if(boardIdxArray == ""){
	   alert("昏力且 亲格阑 急琶秦林技夸.");
	   return false;
	} else{
	    $("#friendArr").attr('value', boardIdxArray); 
		$('#reqFriendFrm').attr('action','/addfriend/delRequestfriend');
	 	$('#reqFriendFrm').submit();
	}
}
	
function allChecked(target){
     if($(target).is(":checked")){
        //眉农冠胶 傈眉 眉农
        $(".friend_chk").prop("checked", true);
     }
     else{
        //眉农冠胶 傈眉 秦力
        $(".friend_chk").prop("checked", false);
     }
  }
      
function cchkClicked(){
       //眉农冠胶 傈眉俺荐
       var allCount = $("input:checkbox[name=friends]").length;
       //眉农等 眉农冠胶 傈眉俺荐
       var checkedCount = $("input:checkbox[name=friends]:checked").length;
       //眉农冠胶 傈眉俺荐客 眉农等 眉农冠胶 傈眉俺荐啊 鞍栏搁 眉农冠胶 傈眉 眉农
       if(allCount == checkedCount){
          $(".friend_chk").prop("checked", true);
       }
       //鞍瘤臼栏搁 傈眉 眉农冠胶 秦力
       else{
          $("#allCheck").prop("checked", false);
       }
 }

</script>            
