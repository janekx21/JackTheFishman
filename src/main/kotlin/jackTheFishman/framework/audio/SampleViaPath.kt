package jackTheFishman.framework.audio

class SampleViaPath(val path: String) : OpenAlSample(getSampleFileViaPath(path))
