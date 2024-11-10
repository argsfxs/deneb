package response;

import util.Status;

/**
 * This class represents a response requesting further input with the status codes 1x.
 */
public class GeminiInputResponse extends GeminiResponse
{
    private final String prompt;
    
    GeminiInputResponse( Status status, String header, String prompt )
    {
        super( status, header );
        this.prompt = prompt;
    }
    
    public String getPrompt()
    {
        return prompt;
    }
}
