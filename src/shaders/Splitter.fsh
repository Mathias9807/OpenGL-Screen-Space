#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;

in vec4 vertex_p, vertex_c, vertex_m;
in vec2 uv_tan;
in vec3 normal_m, normal_c;

uniform sampler2D texture0;
uniform mat4 proj, view, model;
uniform float time;


void main() {
	vec4 tex0 = texture(texture0, uv_tan);
	
	vec3 normal_ic = normalize(normal_c);
    
    color0 = vec4(normal_ic, gl_FragCoord.z);
    color1 = vec4(tex0.rgb, 1);
}
