import os

class AudioFile:
    def __init__(self, filename):
        if not filename.endswith(self.ext):
            raise Exception("format nesuportat")
        self.filename = filename

class MP3File(AudioFile):
    ext = "mp3"

    def play(self):
        print("se canta {} un mp3".format(self.filename))

class WavFile(AudioFile):
    ext = "wav"

    def play(self):
        print("se canta {} un wav".format(self.filename))


class OggFile(AudioFile):
    ext = "ogg"

    def play(self):
        print("se canta {} un ogg".format(self.filename))


class FlacFile:
    def __init__(self, filename):
        if not filename.endswith(".falc"):
            raise Exception("format necunoscut")
        self.filename = filename

    def play(self):
        print("se canta {} un flac".format(self.filename))


if __name__ == '__main__':
    tipuri={
        ".mp3":MP3File,
        ".wav": WavFile,
        ".ogg": OggFile,
        ".flac": FlacFile
    }#dictionar
    pathname = input("pathname =")
    #pathname='/home/student/Desktop/empty/fisier.mp3'
    if os.path.isfile(pathname):
        ext = os.path.splitext(pathname)[1]#obtine extensia, .mp3
        if ext in tipuri:
            audio=tipuri[ext](pathname)#apel constructor cu agr pathname
            audio.play()
    else:
        print("nu exista fisierul cu path ul descris")

