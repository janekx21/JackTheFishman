package jackTheFishman.engine.audio

class SampleViaPath(val path: String) : Sample(getSampleFileViaPath(path))