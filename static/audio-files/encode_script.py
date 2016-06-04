#
# ffmpeg -i <input-file> -ac 2 -codec:a libmp3lame -b:a 48k -ar 16000 <output-file>
#
import os
from os import listdir
from os.path import isfile, join
onlyfiles = [f for f in listdir('./') if isfile(join('./', f))]
onlyfiles = [x for x in onlyfiles if '.wav' == x[-4:].lower()]


print("\n================{0} .wav Audio files to decode================".format(str(len(onlyfiles))))
for x in onlyfiles:
	print x

commands = []
for x in onlyfiles:
	commands.append("ffmpeg -i {0} -ac 2 -codec:a libmp3lame -b:a 48k -ar 16000 encoded-for-echo/{1}".format(x,x.replace('.wav','.mp3')))
print("\n================{0} ffmpeg commands ================".format(str(len(commands))))
for x in commands:
	print x

for command in commands:
	os.system(command)