<?php

/*

$q = $_GET['q'];

$url = "http://api.wolframalpha.com/v2/query?input=".urlencode($q)."&appid=U4UG8T-TL42PVU3UA"; 

echo $url."<br><br>--------------------------------------<br><br>";


$curl = curl_init();  
curl_setopt($curl, CURLOPT_URL,$url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_TIMEOUT_MS, 90000);
curl_setopt($curl, CURLOPT_PROXY, "netmon.iitb.ac.in:80");
curl_setopt($curl, CURLOPT_PROXYUSERPWD,"vaibhavtolia:akanksha143$");

$result = curl_exec ($curl);
$error = curl_error($curl);
curl_close($curl);


echo $error;

$xml = simplexml_load_string($result);


$a = (array) $xml;

if($a['@attributes']['success'] == 'true'){

	foreach($xml->pod as $q){
		echo $q->subpod->plaintext."<br>-------------------------<br>";
	}
}

else{
	echo "###########################################################<br><br> ";	
	print_r(simplexml_load_string($result));
}


*/

class SearchAnswers {

	private $API_KEY = "" , $URL = "" , $search_string = "" , $search_suggestions = array() , $search_count = 0, $question = "", $options = array() , $search_index = 0; 

	function __construct(){
		$this->API_KEY = "U4UG8T-TL42PVU3UA";
		$this->URL = "http://api.wolframalpha.com/v2/query?";
	}

	public function makeSearch($question,$options){
		$this->question = $question;
		$this->options = $options;
		$r = $this->Search($question);
		return $r;
	}

	private function makeCurlRequest(){
		$url = $this->URL."input=". urlencode($this->search_string)."&appid=".$this->API_KEY;
		$curl = curl_init();  
		curl_setopt($curl, CURLOPT_URL,$url);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($curl, CURLOPT_TIMEOUT_MS, 90000);
		//curl_setopt($curl, CURLOPT_PROXY, "netmon.iitb.ac.in:80");
		//curl_setopt($curl, CURLOPT_PROXYUSERPWD,"vaibhavtolia:akanksha143$");
		$result = curl_exec ($curl);
		$error = curl_error($curl);
		curl_close($curl);
		if(isset($result)){
			return array('status'=>1,'data'=>$result);
		}
		else{
			return array('status'=>0,'message'=>"Curl request failed with error : ".$error);
		}
	}

	private function Search($q){
		if(isset($q) && strlen($q) >= 3){
			if($this->search_count == 0){
				$q = $this->filterQuestion($q);
			}
			$this->search_count++;
			$this->search_string = $q;
			//echo "\n".$q."\n";
			$result = $this->makeCurlRequest();
			//print_r($result['data']);
			//echo "<br>";
			if($result['status'] == 1){
				$xml = $result['data'];
				$res = $this->parseXML($xml);
				if($res['status'] == 1){
					$this->writeLog();
					return array('status'=>1,'data'=>$res['data']);
				}
				elseif($res['status'] == 2) {
					$res = $this->repeatSearch();
					$this->writeLog();
					return $res;
				}
				else{
					$this->writeLog();
					return array('status'=>0,'data'=>null,'message'=>"Unable to find answer to your question");
				}
			}
			else{
				$this->writeLog();
				return $result;
			}
		}
		else{
			$this->writeLog();
			return array('status'=>0,'message'=>"Invalid search string, must be greater than 2 characters");
		}
	}
	
	private writeLog(){
		$a = "Question : ".$this->question."\n\noptions - ".print_r($this->options)."\n\nTotal Search : ".$this->search_count."\n\nSuggestions : ".$this->search_suggestions."\n---------------------------\n\n";
		$fh = fopen("log.txt", 'a');
		fwrite($fh, $a);
		fclose($fh);
	}
	
	private function repeatSearch(){
		if(isset($this->search_suggestions) && $this->search_suggestions != null){
			$q = $this->search_suggestions[0];
			$return_value = $this->Search($q);
			if($return_value['status'] == 1){
				return $return_value;
			}
			else{
				return array('status'=>0,'data'=>null,'message'=>"Unable to find answer to your question");
			}
		}
		else{
			return array('status'=>0,'data'=>null,'message'=>"Unable to find answer to your question");
		}
	}
	
	private function parseXML($xml){
		$result_object = simplexml_load_string($xml);
		$a = (array)$result_object;
		if($a['@attributes']['success'] == 'true'){
			$j = 0;
			$answer_data = array();
			$answer_string = "";
			foreach($result_object->pod as $q){
				$answer_data[$j] =  $q->subpod->plaintext;
				$answer_string .= $q->subpod->plaintext[0]." ";
				//echo $q->subpod->plaintext;
				$j++;
			}
			//echo $answer_string;
			//print_r($answer_data);
			$match_answer = $this->matchAnswer($answer_string);
			if($match_answer['status'] == 1){
				return array('status'=>1,'data'=>$match_answer['answer']);
			}
			else{
				return array('status'=>2,'data'=>null,'message'=>"No match to answers, search for other suggestions"); 
			}
		}
		else{
			$did_you_mean = $a['didyoumeans'];
			$alternative_search = $did_you_mean->didyoumean;
			//print_r($alternative_search); 
			do{
				$q = $alternative_search[$this->search_index];
				$r = $this->Search($q);
				$this->search_index++;
			}while($r['status'] == 0 && $this->search_index < count($alternative_search));
			if($r['status'] == 1){
				return array('status'=>1,'data'=>$r['data']);
			}
			else{
				return array('status'=>0,'data'=>null);
			}
		}
	}
	
	function filterQuestion($question){
		$a = array("'tis","'twas","a","able","about","across","after","ain't","all","almost","also","am","among","an","and","any",
"are","aren't","as","at","be","because","been","but","by","can","can't","cannot","could","could've","couldn't","dear","did",
"didn't","do","does","doesn't","don't","either","else","ever","every","for","from","get","got","had","has","hasn't","have",
"he","he'd","he'll","he's","her","hers","him","his","how","how'd","how'll","how's","however","i","i'd","i'll","i'm","i've",
"if","in","into","is","isn't","it","it's","its","just","least","let","like","likely","may","me","might","might've",
"mightn't","most","must","must've","mustn't","my","neither","no","nor","not","of","off","often","on","only","or","other","our"
,"own","rather","said","say","says","shan't","she","she'd","she'll","she's","should","should've","shouldn't","since","so","some",
"than","that","that'll","that's","the","their","them","then","there","there's","these","they","they'd","they'll","they're",
"they've","this","tis","to","too","twas","us","wants","was","wasn't","we","we'd","we'll","we're","were","weren't","what","what'd",
"what's","when","when","when'd","when'll","when's","where","where'd","where'll","where's","which","while","who","who'd","who'll",
"who's","whom","why","why'd","why'll","why's","will","with","won't","would","would've","wouldn't","yet","you","you'd","you'll",
"you're","you've","your","known","came","aboard","about","above","across","after","against","along","amid","among","anti","around",
"as","at","before","behind","below","beneath","beside","besides","between","beyond","but","by","concerning","considering",
"despite","down","during","except","excepting","excluding","following","for","from","in","inside","into","like","minus","near",
"of","off","on","onto","opposite","outside","over","past","per","plus","regarding","round","save","since","than","through","to",
"toward","towards","under","underneath","unlike","until","up","upon","versus","via","with","within","without","The","the");
	
		$s = explode(" ",$question);
		$filetered_question = "";
		foreach($s as $k){
			if(in_array(trim($k),$a)){ $k = ""; }
			$filetered_question .= $k." ";
		}
		return $filetered_question;
	}
	
	private function matchAnswer($answer_string){			//$answer_string is the string from array of answers
		//echo "\n---------------------------------------\n".$answer_string."\n---------------------------------------\n";
		$option_count = array();
		$option_percent_match = array();
		$answer_string = strtolower($answer_string);
		$i = 0; 
		foreach($this->options as $option){
			$option_count[$i] = 0;
			$option = $this->filterQuestion($option);
			$sub_option = explode(" ",$option);
			foreach($sub_option as $op){
				if($op != null && $op != ""){
					//echo "\n".$op."\n";
					$option_count[$i] += substr_count($answer_string,strtolower($op));
				}
				//echo $option_count[$i]."\n";
			}
			$i++;
		}
		//print_r($option_count);
		$total_count = 0;
		foreach($option_count as $k){
			$total_count += $k;
		}
		if($total_count != 0){
			for($j = 0; $j<count($option_count); $j++){
				$option_percent_match[$j] = $option_count[$j]/$total_count*100;
			}
			$a = max($option_percent_match);
			$key = array_search($a,$option_percent_match);
			//echo "\n-------------------------------------\n".$key."\n-----------------------------------\n";
			//print_r($this->options);
			$answer = $this->options[$key];
			return array('status'=>1,'answer'=>$answer);
		}
		else{
			return array('status'=>0,'data'=>null,'message'=>"No match occured for options with seach result");
		}
	}
	
}

/*

$obj = new SearchAnswers();

$q = "Garampani sanctuary is located at";

$options = array(
	"Junagarh, Gujarat",
	"Diphu, Assam",
	"Kohima, Nagaland",
	"Gangtok, Sikkim"
);

$r = $obj->makeSearch($q,$options);

print_r($r);


*/
?>