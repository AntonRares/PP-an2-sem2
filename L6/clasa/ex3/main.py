class AudioFile:
    def __init__(self,filename):
        if not filename.endswith(self.ext):
            raise Exception("format nesuportat")
        self.filename=filename

class MP3File(AudioFile):
    ext = "mp3"

    def play(self):
        print("se canta {} un mp3".format(self.filename))

class WavFile(AudioFile):
    ext="wav"

    def play(self):
        print("se canta {} un wav".format(self.filename))

class OggFile(AudioFile):
    ext="ogg"

    def play(self):
        print("se canta {} un ogg".format(self.filename))

class FlacFile:
    def __init__(self,filename):
        if not filename.endswith(".falc"):
            raise Exception("format necunoscut")
        self.filename=filename

    def play(self):
        print("se canta {} un flac".format(self.filename))

if __name__=='__main__':
    playlist=[MP3File("rock.mp3"),WavFile("sunet_natura.wav"),FlacFile("opera.falc")]
    for fisier in playlist:
        fisier.play()
    