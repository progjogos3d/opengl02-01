package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import br.pucpr.mage.Keyboard;
import br.pucpr.mage.Scene;
import br.pucpr.mage.Shader;
import br.pucpr.mage.Window;

/**
 * Essa classe demonstra como desenhar um triangulo na tela utilizando a OpenGL.
 */
public class RotatingTriangle implements Scene {
	private Keyboard keys = Keyboard.getInstance();

	/** Esta variável guarda o identificador da malha (Vertex Array Object) do triângulo */
	private int vao;

	/** Guarda o id do buffer com todas as posições do vértice. */
	private int positions;

	/** Guarda o id do shader program, após compilado e linkado */
	private int shader;

	@Override
	public void init() {
		//Define a cor de limpeza da tela
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//------------------
		//Criação da malha
		//------------------

		//O processo de criação da malha envolve criar um Vertex Array Object e associar a ele um buffer, com as
		// posições dos vértices do triangulo.

		//Criação do Vertex Array Object (VAO)
		vao = glGenVertexArrays();

		//Informamos a OpenGL que iremos trabalhar com esse VAO
		glBindVertexArray(vao);


		//Criação do buffer de posições
		//------------------------------

		//Criamos um array no java com as posições. Você poderia ter mais de um triângulo nesse mesmo
		//array. Para isso, bastaria definir mais posições.
		float[] vertexData = new float[] {
				0.0f,  0.5f,
				-0.5f, -0.5f,
				0.5f, -0.5f
		};

		//Solicitamos a criação de um buffer na OpenGL, onde esse array será guardado
		positions = glGenBuffers();
		//Informamos a OpenGL que iremos trabalhar com esse buffer
		glBindBuffer(GL_ARRAY_BUFFER, positions);

		//Damos o comando para carregar esses dados na placa de vídeo
		//o parametro GL_STATIC_DRAW indica que não mexeremos mais nos valores desses dados em nossa aplicação
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

		//Como já finalizamos a carga, informamos a OpenGL que não estamos mais usando esse buffer.
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		//Finalizamos o nosso VAO, portanto, informamos a OpenGL que não iremos mais trabalhar com ele
		glBindVertexArray(0);

		//------------------------------
		//Carga/Compilação dos shaders
		//------------------------------

		shader = Shader.loadProgram("basic.vert", "basic.frag");
	}

	@Override
	public void update(float secs) {
		//Testa se a tecla ESC foi pressionada
		if (keys.isPressed(GLFW_KEY_ESCAPE)) {
			//Fecha a janela, caso tenha sido
			glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
			return;
		}
	}

	@Override
	public void draw() {
		//Solicita a limpeza da tela
		glClear(GL_COLOR_BUFFER_BIT);

		//Precisamos dizer qual VAO iremos desenhar
		glBindVertexArray(vao);

		//E qual shader program irá ser usado durante o desenho
		glUseProgram(shader);

		//Procuramos o identificador do atributo de posição
		int aPosition = glGetAttribLocation(shader, "aPosition");

		//Informamos a OpenGL que iremos trabalhar com essa variável
		glEnableVertexAttribArray(aPosition);

		//Informamos ao OpenGL que também trabalharemos com o buffer de posições
		glBindBuffer(GL_ARRAY_BUFFER, positions);

		//Chamamos uma função que associa as duas.
		glVertexAttribPointer(aPosition, 2, GL_FLOAT, false, 0, 0);

		//Comandamos o desenho de 3 vértices
		glDrawArrays(GL_TRIANGLES, 0, 3);

		//Faxina
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(aPosition);
		glBindVertexArray(0);
		glUseProgram(0);
	}

	@Override
	public void deinit() {
	}

	public static void main(String[] args) {
		new Window(new RotatingTriangle()).show();
	}
}