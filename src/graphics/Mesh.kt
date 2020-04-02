package graphics

import IDrawable
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.opengl.GL46.*


class Mesh(private val data: FloatArray) : IDrawable {
    private val vbo: Int = glGenBuffers()
    private val vao: Int = glGenVertexArrays()
    private val shader: Shader =
        Shader("assets/shaders/fragment.glsl", "assets/shaders/vertex.glsl").also { println(it.toString()) }

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)

        with(glGetAttribLocation(shader.program, "position")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 8 * 4, 0)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "z")) {
            glVertexAttribPointer(this, 1, GL_FLOAT, false, 8 * 4, 2 * 4)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "normal")) {
            glVertexAttribPointer(this, 3, GL_FLOAT, false, 8 * 4, 3 * 4)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "uv")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 8 * 4, 6 * 4)
            glEnableVertexAttribArray(this)
        }
        glBindVertexArray(0)
    }

    constructor(path: String) : this(loadModel(path))

    companion object {
        fun loadModel(path: String): FloatArray {
            val scene =
                aiImportFile(path, aiProcess_Triangulate or aiProcess_GenNormals)
            check(scene != null) { "scene could not be loaded" }
            val fl = arrayListOf<Float>()
            for (meshIndex in 0 until scene.mNumMeshes()) {
                val x = scene.mMeshes()?.get(meshIndex)
                check(x != null) { "mesh could not be found" }
                val mesh = AIMesh.create(x)

                for (faceIndex in 0 until mesh.mNumFaces()) {
                    val face = mesh.mFaces().get(faceIndex)
                    for (vertexIndex in 0 until face.mNumIndices()) {
                        val index = face.mIndices().get(vertexIndex)
                        val vec = mesh.mVertices().get(index)
                        fl.add(vec.x())
                        fl.add(vec.y())
                        fl.add(vec.z())
                        val normal = mesh.mNormals()?.get(index)
                        check(normal != null) { "model has no normals" }
                        fl.add(normal.x())
                        fl.add(normal.y())
                        fl.add(normal.z())
                        val uv = mesh.mTextureCoords(0)?.get(index)
                        check(uv != null) { "model has no uvs" }
                        fl.add(uv.x())
                        fl.add(uv.y())
                    }
                }
            }
            return fl.toFloatArray()
        }
    }

    override fun draw() {
        glBindVertexArray(vao)
        shader.bind()
        glDrawArrays(GL_TRIANGLES, 0, data.size / 3)
        shader.unbind()
        glBindVertexArray(0)
    }
}