<?php 


	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";



	$preid = 111908491436059750060;


	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	//echo "Connected successfully <br>";

    //$sql = "SELECT * FROM users WHERE u_identity = 12345";
    $sql = "SELECT * FROM users";
	$result = $conn->query($sql);

	while($r = mysqli_fetch_assoc($result)) {
		$rows[] = $r;
	}
	print json_encode($rows);


	$conn->close();
?>

