#version 330

layout(location = 0) in vec3 vertex_in;

uniform mat4 shadowMat, model;

void main() {
    gl_Position = shadowMat * model * vec4(vertex_in, 1);
}
