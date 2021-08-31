import React from 'react'

import { Editor } from '@tinymce/tinymce-react';

export default function TinyMCE() {
    const handleEditorChange = (e) => {
        console.log(
            'Content was updated:',
            e.target.getContent()
        );
    }
    return (
        <div>
            <svg x={12} y={4} width={24} height={24} viewBox="0 0 1024 1024" fill="#666">
                <path d={1} />
            </svg>
            <Editor
                initialValue="<p>Initial content</p>"
                // create tiny account for get api key
                apiKey="06thvnwgx9f8uhkccnayvxkw779wdzgg1xu2mbj0cewjh5yu"
                init={{
                    height: 500,
                    menubar: true,
                    selector: 'textarea',
                    plugins:
                        [
                            'a11ychecker advcode casechange export formatpainter linkchecker',
                            'advlist autolink lists link image charmap print preview anchor',
                            'searchreplace visualblocks code fullscreen',
                            'insertdatetime media table paste imagetools wordcount',
                            ' tinycomments tinymcespellchecker emoticons charmap'
                        ],
                    // 'a11ychecker advcode casechange export formatpainter linkchecker autolink lists checklist media mediaembed pageembed permanentpen powerpaste table advtable tinycomments tinymcespellchecker emoticons imagetools',
                    toolbar:
                        'undo redo | formatselect | emoticons | bold italic | link image |\
                         alignleft aligncenter alignright | \
                         bullist numlist outdent indent | help | a11ycheck addcomment showcomments casechange checklist code export | formatpainter pageembed permanentpen table charmap',
                    toolbar_mode: 'floating',
                    tinycomments_mode: 'embedded',
                    tinycomments_author: 'Author name',
                    //         toolbar:
                    //             'undo redo | formatselect | bold italic | \
                    // alignleft aligncenter alignright | \
                    // bullist numlist outdent indent | help'
                }}
                onChange={handleEditorChange}
            />
        </div>
    )
}
