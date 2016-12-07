﻿using UnityEngine;
using System.Collections;

public class CellScript : MonoBehaviour {

    public Sprite txtr = null;
    public bool is_road = false;
    public bool is_buildable = false;


    public GameObject tower = null;

	// Use this for initialization
    void Start()
    {
        if (txtr)
        {
            gameObject.GetComponent<SpriteRenderer>().sprite = txtr;
        }
    }
	

    void OnMouseEnter()
    {
        gameObject.GetComponent<SpriteRenderer>().color = Color.blue;
    }

    void OnMouseExit()
    {
        gameObject.GetComponent<SpriteRenderer>().color = Color.white;
    }

    void OnMouseUp()
    {
        if (!is_buildable) { return; }
        gameObject.GetComponent<SpriteRenderer>().color = Color.yellow;
        var menu = GameObject.Find("CELLMENU");
        menu.GetComponent<CellMenu>().pos = gameObject.GetComponent<Transform>().position;
        menu.GetComponent<Transform>().position = new Vector3 (gameObject.GetComponent<Transform>().position.x, gameObject.GetComponent<Transform>().position.y, -1);
        


    }
}
