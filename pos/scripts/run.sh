#!/bin/bash
if [[ $1 == "1" ]]
then
  if [[ $2 = "hmm" ]]
  then
    java -cp "mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" cc.mallet.fst.HMMSimpleTagger --train true --model-file hmm.model --training-proportion 0.8 --test lab data/$3.txt
  else
    java -cp "mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger --train true --model-file crf.model --training-proportion 0.8 --test lab data/$3.txt
  fi
else
  for i in {0,$1}
  do
    if [[ $2 = "hmm" ]]
    then
      java -cp "mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" cc.mallet.fst.HMMSimpleTagger --train true --model-file hmm.model --random-seed $RANDOM --test lab data/$3.txt
    else
      java -cp "mallet-2.0.8RC3/class:mallet-2.0.8RC3/lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger --train true --model-file crf.model --random-seed $RANDOM --test lab data/$3.txt
    fi
  done
fi