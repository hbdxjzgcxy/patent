<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp" %>
<form id="modify-pass-form" method="post"	action="<%=path %>/user/modifypass.html" class="form-horizontal">
						<input name="id" value="${user.id }" type="hidden" />
	<div class="right_sider">
		<div class="Rwrap">
			<div class="container">
				<div class="nav" style="margin-bottom: 15px;padding-left:10px;">
					<a href="#">用户信息管理</a> &gt; <span>修改密码</span>
				</div>
				<!--Begin block2-->
				<div class="block2">
					<div class="lists">
						<div class="wrap">
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
										<td style="width:60px;">原密码</td>
										<td><input name="oldpass" type="password" required/>
										</td>
									</tr>
									<tr>
										<td>密码</td>
										<td><input name="password" type="password" required/>
										</td>
									</tr>
									<tr>
										<td>确认密码</td>
										<td><input name="repass" type="password" required/>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!--End block2-->
				<!--Begin submit1-->
				<div class="submit1 submitBG1">
					<input type="submit"  value="修改" class="btn1">
				</div>
				<!--End submit1-->
			</div>
		</div>
	</div>
	<input type="hidden" name="searchType" value="table" />
</form>
<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<script type="text/javascript">

//prepare the form when the DOM is ready 
$(document).ready(function() {
    var options = { 
        beforeSubmit:  validate,  // pre-submit callback 
        success:       showResponse  // post-submit callback 
 
        // other available options: 
        //url:       url         // override for form's 'action' attribute 
        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
        //clearForm: true        // clear all form fields after successful submit 
        //resetForm: true        // reset the form after successful submit 
 
        // $.ajax options can be used here too, for example: 
        //timeout:   3000 
    }; 
 
    // bind form using 'ajaxForm' 
    $('#modify-pass-form').ajaxForm(options); 
}); 
 
// pre-submit callback 
function validate(formData, jqForm, options) { 
    // formData is an array; here we use $.param to convert it to a string to display it 
    // but the form plugin does this for you automatically when it submits the data 
//     var queryString = $.param(formData); 
 
    // jqForm is a jQuery object encapsulating the form element.  To access the 
    // DOM element for the form do this: 
    var formElement = jqForm[0];
    
    
    var oldpass = formElement.oldpass.value;
	var password = formElement.password.value;
	var repass = formElement.repass.value;
	
	if(!oldpass){
			alert('请输入原密码');
		return false;
	}
	
	if(!password){
			alert('请输入新密码');
		return false;
	}
	
	if(!repass){
			alert('请输入确认密码');
		return false;
	}
	
	if(password != repass){
		alert('确认密码和新密码不一致');
		return false;
	} 
 
    // here we could return false to prevent the form from being submitted; 
    // returning anything other than false will allow the form submit to continue 
    return true; 
} 
 
// post-submit callback 
function showResponse(responseText, statusText, xhr, $form)  { 
    // for normal html responses, the first argument to the success callback 
    // is the XMLHttpRequest object's responseText property 
 
    // if the ajaxForm method was passed an Options Object with the dataType 
    // property set to 'xml' then the first argument to the success callback 
    // is the XMLHttpRequest object's responseXML property 
 
    // if the ajaxForm method was passed an Options Object with the dataType 
    // property set to 'json' then the first argument to the success callback 
    // is the json data object returned by the server 
 
    //alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + '\n\nThe output div should have already been updated with the responseText.'); 
    
	if(responseText && responseText != 'success'){
 	   alert(responseText);
    }else{
 	   $('#modify-pass-form').clearForm();
 	   alert('密码修改成功');
    }
}
   
   
</script>

