#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;
layout(location = 2) out vec4 color2;

in vec4 vertex_p, vertex_c, vertex_w;
in vec2 uv_tan;
in vec3 normal_w, normal_c;
in mat3 tangent;

uniform sampler2D texture0, texture1;
uniform mat4 proj, view, model;
uniform float time;


void main() {
	vec4 tex0 = texture(texture0, uv_tan), 
		 tex1 = texture(texture1, uv_tan);
	
	vec3 normalSample = tex1.rgb * 2 - 1;
	normalSample *= vec3(-1, 1, 1);
	vec3 normal_iw = normalize(tangent * normalSample);
    
    color0 = vec4(normal_iw, gl_FragCoord.z);
    color1 = vec4(pow(tex0.rgb, vec3(2.2)), 1);
    color2 = vec4(vertex_w);
}
