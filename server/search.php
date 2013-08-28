<?php

require_once('api.php');

$q = $_REQUEST['q'];

$res = explode("#|&|#",$q);


$question = $res[0];

$options = array_splice($res);

$obj = new SearchAnswers();


$result = $obj->makeSearch($question,$options);

echo json_encode($result);


?>