<?php
include_once 'pt_function.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json = $_POST["syncsts"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
//Decode JSON into an Array
$data = json_decode($json);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->updateSyncSts($data[$i]->groupMember,$data[$i]->status ,$data[$i]->groupId );
	//Based on inserttion, create JSON response
	if($res){
		$b["groupMember"] = $data[$i]->groupMember;
		$b["status"] = 'yes';
		array_push($a,$b);
	}else{
		$b["groupMember"] = $data[$i]->groupMember;
		$b["status"] = 'no';
		array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);
?>