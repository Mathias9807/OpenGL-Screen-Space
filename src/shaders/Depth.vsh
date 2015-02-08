#version 330

layout(location = 0) in vec3 vertex_in;

uniform mat4 proj, view, model;

void main() {
    gl_Position = proj * view * model * vec4(vertex_in, 1);
}
