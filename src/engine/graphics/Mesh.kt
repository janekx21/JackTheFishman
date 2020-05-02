package engine.graphics

import engine.IDrawable
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp
import org.lwjgl.opengl.GL46.*


class Mesh(private val data: Array<Vertex>) : IDrawable {
    private val vbo: Int = glGenBuffers()
    private val vao: Int = glGenVertexArrays()

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)

        val buffer = data.flatMap { vertex -> vertex.toBuffer() }.toFloatArray()
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)

        Vertex.vertexAttribute()
        glBindVertexArray(0)
    }

    fun use(callback: () -> Unit) {
        glBindVertexArray(vao)
        callback()
        glBindVertexArray(0)
    }

    override fun draw() {
        this.use {
            glDrawArrays(GL_TRIANGLES, 0, data.size)
        }
    }

    companion object {
        fun createViePath(path: String): Mesh {
            val scene =
                Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate or Assimp.aiProcess_GenNormals)
            check(scene != null) { "scene could not be loaded" }

            println("there are ${scene.mNumMeshes()} meshes")

            val fl = arrayListOf<Vertex>()
            for (i in 0 until scene.mNumMeshes()) {
                val x = scene.mMeshes()?.get(i)
                if (x !== null) {
                    val mesh = AIMesh.create(x)
                    println("vertex len is ${mesh.mNumVertices()}")
                    println("num of faces is ${mesh.mNumFaces()}")

                    for (j in 0 until mesh.mNumFaces()) {
                        val face = mesh.mFaces().get(j)
                        for (k in 0 until face.mNumIndices()) {
                            val index = face.mIndices().get(k)
                            val vec = mesh.mVertices().get(index)
                            val uv = mesh.mTextureCoords(0)?.get(index)
                            check(uv != null) { "no uv prvided" }
                            val normal = mesh.mNormals()?.get(index)
                            check(normal != null) { "no normal provied" }
                            fl.add(
                                Vertex(
                                    Vector3f(vec.x(), vec.y(), vec.z()),
                                    Vector2f(uv.x(), uv.y()),
                                    Vector3f(normal.x(), normal.y(), normal.z())
                                )
                            )
                        }
                    }
                }
            }

            return Mesh(fl.toTypedArray())
        }
    }
}