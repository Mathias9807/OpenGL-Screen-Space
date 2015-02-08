#version 330

layout(location = 0) in vec3 vertex_in;
layout(location = 1) in vec2 uv_in;

out vec4 vertex_m;
out vec2 uv_tan;

void main() {
	vertex_m = vec4(vertex_in, 1);
	uv_tan = uv_in;
    gl_Position = vertex_m;
}
