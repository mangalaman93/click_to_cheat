<?php

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
"toward","towards","under","underneath","unlike","until","up","upon","versus","via","with","within","without");

$sentence  = "Entomology is the science that studies";

echo $sentence;

echo "<br>###########################################################<br><br>";

$s = explode(" ",$sentence);
$p = "";
foreach($s as $k){
	//echo $k."   /    ";
	if(in_array(trim($k),$a)){ $k = ""; }
	$p .= $k." ";
}

echo $p."<br>";
/*
echo "###########################################################<br><br>";	

$newphrase = str_replace($a, "", $sentence);

echo $newphrase;
*/

?>