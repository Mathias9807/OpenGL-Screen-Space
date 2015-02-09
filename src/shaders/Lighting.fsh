#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;

in vec4 vertex_m;
in vec2 uv_tan;

uniform sampler2D texture0, texture1, texture2;
uniform mat4 proj, view;


void main() {
	vec4 tex0 = texture(texture0, uv_tan), 
		 tex1 = texture(texture1, uv_tan);
	vec3 normal = tex0.rgb;
	
	vec3 blinnPhong = vec3(tex1.rgb * 0.2);
	
	blinnPhong += max(
		dot(
			normal, 
			normalize(vec3(1, 1, 1))
		), 
	0) * tex1.rgb * 2.5;
	
    color0 = tex0;
    color1 = vec4(blinnPhong.rgb, tex1.a);
}
