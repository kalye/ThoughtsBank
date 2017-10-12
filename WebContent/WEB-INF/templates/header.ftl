 <header>
       <span style="display:inline-block; float:left;">Thoughts Bank Application</span>
       <span style="display:inline-block; float:right;">
        <#if userName?? >
        Welcome ${userName}
 			
	 	<#else>
 			"Not Logged In"
		</#if>
       		
		</span>
 </header>