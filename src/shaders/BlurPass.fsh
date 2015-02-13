#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;

in vec4 vertex_m;
in vec2 uv_tan;

uniform sampler2D texture0, texture1, data0, data1;
uniform mat4 proj, view;
uniform vec2 dir;
uniform float radius = 4, scaling = 1;
uniform bool finalPass;

void main() {
	vec4 tex0 = texture(texture0, uv_tan), 
		 tex1 = texture(texture1, uv_tan);
	vec2 texel = float(1) / textureSize(texture0, 0);
	vec3 normal = texture(data0, uv_tan).rgb;
	
	float shadow = 0, totalStrength = 0;
	for (float f = -radius; f < radius; f++) {
		float sample = texture(texture0, uv_tan + dir * f * texel * scaling).a;
		
		float strength = 1 - abs(f / radius);
		strength *= strength;
		
		totalStrength += strength;
		shadow += sample * strength;
	}
	shadow /= totalStrength;
	
	vec3 blur = vec3(tex0.rgb);
	/*totalStrength = 1;
	for (float f = -radius; f < radius; f++) {
		vec2 uv = uv_tan + dir * f * texel * scaling;
		if (abs(uv.s - 0.5) > 0.5 && abs(uv.t - 0.5) > 0.5) continue;
		vec3 sample = texture(texture0, uv).rgb;
		
		float strength = 1 - abs(f / radius);
		strength *= strength;
		
		totalStrength += strength;
		blur += sample * strength;
	}
	blur /= totalStrength;*/
	
	if (finalPass) {
		vec3 sun = texture(data1, uv_tan).rgb * max(
			dot(
				normal, 
				normalize(vec3(1))
			), 
		0) * clamp(1 - shadow, 0, 1) * 5;
		vec3 finalColor = blur + sun;
		finalColor = pow(finalColor, vec3(1.0) / 2.2);
    	color0 = vec4(((finalColor) / ((finalColor) + 1)), 1);
    }else {
    	color0 = vec4(blur, shadow);
    }
}
