#version 330

layout(location = 0) in vec3 vertex_in;
layout(location = 1) in vec2 uv_in;
layout(location = 2) in vec3 normal_in;

out vec4 vertex_p, vertex_c, vertex_w;
out vec2 uv_tan;
out vec3 normal_w, normal_c;
out mat3 tangent;

uniform mat4 proj, view, model;

void main() {
	vertex_w 	= model * vec4(vertex_in, 1);
	vertex_c 	= view * vertex_w;
	vertex_p 	= proj * vertex_c;
    gl_Position = vertex_p;
    
	uv_tan 		= vec2(uv_in.s, 1 - uv_in.t);
	
	normal_w	= normalize(mat3(model) * normal_in);
	normal_c	= normalize(mat3(view) * normal_w);
	
	vec3 x;
	if (normal_w.y == 1) x = cross(vec3(0, 0, -1), normal_w);
	else if (normal_w.y == -1) x = cross(vec3(0, 0, 1), normal_w);
	else x = cross(vec3(0, 1, 0), normal_w);
	vec3 y = cross(normal_w, x);
	tangent = mat3(
		x, 
		y, 
		normal_w
	);
}
