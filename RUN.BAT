@ECHO OFF

set /p imgS=Enter Image Size:
set /p hlS=Enter # of Hidden Nodes:
set /p epN=Enter Enter # of Epochs:
set /p lrnR=Enter Learning Rate:
set /p swpR=Enter Swap Rate:

java -cp bin neuralnetwork.BackProp %imgS% %hlS% %epN% %lrnR% %swpR%

@PAUSE
