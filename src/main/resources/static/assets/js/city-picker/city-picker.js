function citypicker(){
   if ($("input.citypicker").length<=0) {
	   return;
   }
   var selHmtl = '<div class="city-picker" style="display: none;">'+
			'<select id="selProvince" size="10" style="width: 120px;">'+
			'</select>'+
			'<select id="selCity" size="10" style="width: 120px;">'+
			'</select>'+
			'<select id="selArea" size="10" style="width: 120px;">'+
			'</select>'+
			'</div>';
    if ($(".city-picker").length<=0) {
        $("input.citypicker").parent().parent().append(selHmtl);
    }

    $("input.citypicker").on("click", function(e){
		$(".city-picker").css("top", $(this).parent().position().top + $(this).parent().height()+2);
		$(".city-picker").css("left", $(this).parent().position().left + $(this).position().left);
		$(".city-picker").toggle();
		e.stopPropagation(); //阻止事件冒泡    
	});
    //点击选择以外的事件隐藏控件
   $(document).on("click",function(e){ 
	   var target = $(e.target); 
	   if(target.closest(".city-picker").length == 0){ 
		   $(".city-picker").hide(); 
	   }
   });
    
   //省市联动
   var city_data = sessionStorage.getItem("city_data");
   if (!city_data) {
		$.get("/area/cache", function(result){
			city_data = JSON.stringify(result.data.sub);
			sessionStorage.setItem("city_data", city_data);
		});
	}
   $("#selProvince").on("change", function(){
   	var sub_data = $(this).find("option:selected").attr("data-sub");
   	if (sub_data){
			getCity(JSON.parse(sub_data));
   	}
   });
   $("#selCity").on("change", function(){
   	var sub_data = $(this).find("option:selected").attr("data-sub");
   	if (sub_data) {
	    	getArea(JSON.parse(sub_data));
   	}
   });
   $("#selArea").on("change", function(){
   	getAreaValue();
   });
   var defId = $("#areaId").val();
   getProvince(JSON.parse(city_data));
   defId = "";
  
  
  function getProvince(data){
		$("#selProvince option").remove();
		for(var i=0; i< data.length; i++){
			$("#selProvince").append("<option value='"+ data[i].id +"' data-sub='"+ JSON.stringify(data[i].sub) +"'>"+ data[i].areaName +"</option>");
		}
	    if (defId) {
	    	$("#selProvince").val(defId.substr(0,2));
	    } else {
	    	$("#selProvince option:first").prop("selected", 'selected');
	    }
		$("#selProvince").trigger("change");
	}
	
	function getCity(data){
		$("#selCity option").remove();
		for(var i=0; i< data.length; i++){
			$("#selCity").append("<option value='"+ data[i].id +"' data-sub='"+ JSON.stringify(data[i].sub) +"'>"+ data[i].areaName +"</option>");
		}
	    if (defId) {
	    	$("#selCity").val(defId.substr(0,4));
	    }else{
			$("#selCity option:first").prop("selected", 'selected');
	    }
		$("#selCity").trigger("change");
	}
	
	function getArea(data){
		$("#selArea option").remove();
		for(var i=0; i< data.length; i++){
			$("#selArea").append("<option value='"+ data[i].id +"'>"+ data[i].areaName +"</option>");
		}
	    if (defId) {
	    	$("#selArea").val(defId);
	    }else{
			$("#selArea option:first").prop("selected", 'selected');
	    }
		$("#selArea").trigger("change");
	}
	
	function getAreaValue(){
  	$("#areaId").val($("#selArea option:selected").val());
	    var txt = $("#selProvince option:selected").text()+"-"+$("#selCity option:selected").text()+"-"+$("#selArea option:selected").text();
	    $("input.citypicker").val(txt);
	    $("input.citypicker").attr("title", txt);
	}
}

