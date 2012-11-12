package psywerx.platformGl.game;

public class Shaders {
    public static final String vertexShader =
         // For GLSL 1 and 1.1 code i highly recomend to not include a 
         // GLSL ES language #version line, GLSL ES section 3.4
         // Many GPU drivers refuse to compile the shader if #version is different from
         // the drivers internal GLSL version.
         "#ifdef GL_ES \n" +
         "precision mediump float; \n" + // Precision Qualifiers
         "precision mediump int; \n" +   // GLSL ES section 4.5.2
         "#endif \n" +

         "uniform mat4    uniform_Projection; \n" + // Incomming data used by
         "uniform mat4    uniform_Model; \n" + // Incomming data used by
         "attribute vec4  attribute_Position; \n" + // the vertex shader
         "attribute vec4  attribute_Color; \n" +    // uniform and attributes

         "varying vec4    varying_Color; \n" + // Outgoing varying data
                                               // sent to the fragment shader
         
         "attribute vec2 a_texCoord; \n" +
         "varying vec2 v_texCoord; \n" +
         
         "void main(void) \n" +
         "{ \n" +
         "  varying_Color = attribute_Color; \n" +
         "  gl_Position = uniform_Projection * uniform_Model * attribute_Position; \n" +
         "  v_texCoord = a_texCoord; \n" +
         "} ";

         /* Introducing the OpenGL ES 2 Fragment shader
          *
          * The main loop of the fragment shader gets executed for each visible
          * pixel fragment on the render buffer.
          *
          *       vertex-> *
          *      (0,1,-1) /f\
          *              /ffF\ <- This fragment F gl_FragCoord get interpolated
          *             /fffff\                   to (0.25,0.25,-1) based on the
          *   vertex-> *fffffff* <-vertex         three vertex gl_Position.
          *  (-1,-1,-1)           (1,-1,-1)
          *
          *
          * All incomming "varying" and gl_FragCoord data to the fragment shader
          * gets interpolated based on the vertex positions.
          *
          * The fragment shader produce and store the final color data output into
          * gl_FragColor.
          *
          * Is up to you to set the final colors and calculate lightning here based on
          * supplied position, color and normal data.
          *
          * The whole fragment shader program are a String containing GLSL ES language
          * http://www.khronos.org/registry/gles/specs/2.0/GLSL_ES_Specification_1.0.17.pdf
          * sent to the GPU driver for compilation.
          */
     public static final String fragmentShader =
         "#ifdef GL_ES \n" +
         "precision mediump float; \n" +
         "precision mediump int; \n" +
         "#endif \n" +
    
         "varying   vec4    varying_Color; \n" + //incomming varying data to the
                                                 //frament shader
         "varying vec2 v_texCoord; \n" +
         "uniform sampler2D s_texture; \n" +
         "uniform float u_isText; \n" +
         "void main (void) \n" +
         "{ \n" +
         "  if(u_isText < 0.5){\n" +
         "    vec4 texture = texture2D(s_texture, v_texCoord.st); \n" +
         "    gl_FragColor = texture + (texture.w * varying_Color); \n" +
         "  } else { \n" +
         //"    gl_FragColor = texture2D(s_texture, v_texCoord.st, 0.0) * varying_Color; \n" +
//         "  vec4 basecolor = texture2D(s_texture, v_texCoord); \n"+
//         "  if(basecolor.a == 0.0) basecolor = varying_Color; \n " +
//         "  gl_FragColor = basecolor; \n" +
         "  gl_FragColor = varying_Color; \n" +
         //"  gl_FragColor = texture2D(s_texture, v_texCoord); \n" +  //"  gl_FragColor = varying_Color; \n" +
         "}} ";
}
