#version 330

layout(location = 0) in vec3 vertex_in;
layout(location = 1) in vec2 uv_in;

out vec2 uv_tan;

void main() {
	uv_tan = uv_in;
    gl_Position = vec4(vertex_in, 1);
}
