#version 330

layout(location = 0) in vec3 vertex_in;
layout(location = 1) in vec2 uv_in;
layout(location = 2) in vec3 normal_in;

out vec4 vertex_p, vertex_c, vertex_m;
out vec2 uv_tan;
out vec3 normal_m, normal_c;

uniform mat4 proj, view, model;

void main() {
	vertex_m 	= model * vec4(vertex_in, 1);
	vertex_c 	= view * vertex_m;
	vertex_p 	= proj * vertex_c;
	uv_tan 		= vec2(uv_in.s, 1 - uv_in.t);
	normal_m	= normalize(mat3(model) * normal_in);
	normal_c	= normalize(mat3(view) * normal_m);
    gl_Position = vertex_p;
}
