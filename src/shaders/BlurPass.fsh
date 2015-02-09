#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;

in vec4 vertex_m;
in vec2 uv_tan;

uniform sampler2D texture0, texture1;
uniform vec2 dir;
uniform float radius = 4, scaling = 8;
uniform bool finalPass;

void main() {
	vec4 tex0 = texture(texture0, uv_tan), 
		 tex1 = texture(texture1, uv_tan);
	vec2 texel = float(1) / textureSize(texture0, 0);
	
	float shadow = 0, totalStrength = 0;
	for (float f = -radius; f < radius; f++) {
		float sample = texture(texture0, uv_tan + dir * f * texel * scaling).a;
		
		float strength = 1 - abs(f / radius);
		strength *= strength;
		
		totalStrength += strength;
		shadow += sample * strength;
	}
	shadow /= totalStrength;
	
	vec3 blur = vec3(0);
	totalStrength = 0;
	for (float f = -radius; f < radius; f++) {
		vec2 uv = uv_tan + dir * f * texel * scaling;
		if (abs(uv.s - 0.5) > 0.5 && abs(uv.t - 0.5) > 0.5) continue;
		vec3 sample = texture(texture0, uv).rgb;
		
		float strength = 1 - abs(f / radius);
		strength *= strength;
		
		totalStrength += strength;
		blur += sample * strength;
	}
	blur /= totalStrength;
	
	if (finalPass) {
		vec3 finalColor = blur * clamp(1 - shadow, 0, 1) + blur;
    	color0 = vec4(((finalColor) / ((finalColor) + 1)), 1);
    }else {
    	color0 = vec4(blur, shadow);
    }
}
