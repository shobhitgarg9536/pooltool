<?php
    require 'ptconnection.php';
  

	$a = array();
	$b = array();
	

		$mysql_qry = "SELECT * FROM register ";

		$result = $conn->query($mysql_qry);

		
    if (mysqli_num_rows($result) > 0){
        
		while ($row = mysqli_fetch_array($result)) {		
			$b["phoneNumber"] = $row["mobileNo"];
			array_push($a,$b);
		}
		
	}

echo json_encode($a);
$conn->close();
?>