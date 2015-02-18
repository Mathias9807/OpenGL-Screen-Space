#version 330

layout(location = 0) out vec4 color0;
layout(location = 1) out vec4 color1;

in vec2 uv_tan;

uniform sampler2D data0, data1, data2;
uniform mat4 proj, view, projInv, viewInv;
uniform bool useBackFaces;

struct Light {
	bool isActive;
	vec3 pos;
	vec3 brightness;
};
uniform Light light[8];

vec4 get3DCoord(vec2 uv);


void main() {
	vec4 tex0 = texture(data0, uv_tan), 
		 tex1 = texture(data1, uv_tan), 
		 tex2 = texture(data2, uv_tan);
	vec3 normal = tex0.rgb;
	vec4 frag = get3DCoord(uv_tan);
	vec4 frag_w = tex2;
	
	vec3 blinnPhong = vec3(tex1.rgb * 0.2);
	vec3 diffuse = vec3(0);
	
	for (int i = 0; i < 8; i++) {
		if (!light[i].isActive) continue;
		vec4 light_w = vec4(light[i].pos, 1);
		vec4 delta_w = light_w - frag_w;
		float dist = length(delta_w.xyz);
		
		float falloff = 1 / (1 + dist * dist);
		
		diffuse += max(
			dot(
				normal, 
				delta_w.xyz / dist
			), 
		0) * light[i].brightness * falloff;
	}
	
	blinnPhong += diffuse * tex1.rgb;
	
	
	float depth = tex0.a;
	vec4 light = normalize(view * vec4(1, 1, 1, 0));
	float scale = 0.1;
	float step = 0.5;
	float length = 20;
	float shadow = 0;
	/*for (float f = 0; f < length; f += step) {
		float strength = f / 20;
		vec4 sample_c = frag + light * scale * f;
		
		vec4 sample_p = proj * sample_c;
		sample_p /= sample_p.w;
		vec2 sample_uv = sample_p.xy / 2 + 0.5;
		
		if (abs(sample_p.s) > 1 || abs(sample_p.t) > 1) break;
		
		float vignette = pow((1 - abs(sample_p.t)) * (1 - abs(sample_p.s)), 0.3);
		
		float geo_depth = texture(texture0, sample_uv).a;
		float geo_far = texture(texture2, sample_uv).r;
		
		if (geo_depth == 0) continue;
		
		float shadowDist = length(sample_c - frag);
		
		if (useBackFaces && sample_p.z + 0.0001 > geo_depth && sample_p.z < geo_far) shadow += 0.5 * strength * clamp(1 / (shadowDist * shadowDist), 0, 1) * vignette;
		else if (sample_p.z + 0.0001 > geo_depth) {
			shadow += 0.2 * clamp(1 / (shadowDist * shadowDist), 0, 1) * vignette * clamp(1 - 50 * (sample_p.z - geo_depth), 0, 1);
		}
	}*/
	
    
    color0 = vec4(blinnPhong.rgb, shadow);
    color1 = vec4(max(blinnPhong.rgb - 2, 0), 1);
}

vec4 get3DCoord(vec2 uv) {
	float z = texture(data0, uv).a;
	float x = uv.s * 2 - 1;
	float y = uv.t * 2 - 1;
	
	vec4 coord = projInv * vec4(x, y, z, 1);
	coord /= coord.w;
	
	return coord;
}
