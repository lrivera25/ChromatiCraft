varying vec2 texcoord;

uniform int time;
uniform int screenWidth;
uniform int screenHeight;
uniform mat4 modelview;
uniform mat4 projection;
uniform float screenX;
uniform float screenY;

void main() {
    vec4 vert = gl_Vertex;
	
	/*
    vert.x = vert.x*1.1;
    vert.y = vert.y*1.0;
    vert.z = vert.z*1.1;
	*/
    
    gl_Position = gl_ModelViewProjectionMatrix * vert;
    texcoord = vec2(gl_MultiTexCoord0);
}