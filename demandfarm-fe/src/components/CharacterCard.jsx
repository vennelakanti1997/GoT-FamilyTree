import { useEffect, useState } from "react";
import { Button, Card, ListGroup } from "react-bootstrap";
import { RequestData } from "../utils/Services";
import {
  ADD_FAVORITE,
  CHARACTER_DETAILS,
  GET_HOUSE_MEMBERS,
} from "../utils/Constants";

export const CharacterCard = ({
  characterName,
  houseName,
  closeCard,
  setHouseMembers,
}) => {
  const [isLoading, setIsLoading] = useState(false);
  const [isFavLoading, setFavIsLoading] = useState(false);
  const [characterData, setCharacterData] = useState({});
  const [id, setId] = useState("");
  const [isAdded, setisAdded] = useState(false);
  useEffect(() => {
    setIsLoading(true);
    setId("");
    setCharacterData({});
    RequestData("GET", `${CHARACTER_DETAILS}/${houseName}/${characterName}`)
      .then((response) => {
        setCharacterData(response.body.data);
        setId(response.body.id);
      })
      .catch((error) => {
        closeCard();
        window.alert(error);
      })
      .finally(() => setIsLoading(false));
  }, []);

  const getHouseMembers = () => {
    RequestData("GET", `${GET_HOUSE_MEMBERS}/${houseName}`)
      .then((response) => {
        setHouseMembers(response.body);
      })
      .catch((error) => window.alert(error));
  };

  const addToFavorites = (id) => {
    setFavIsLoading(true);
    RequestData("PUT", `${ADD_FAVORITE}`.replace("{characterId}", id))
      .then((response) => {
        setisAdded(true);
        getHouseMembers();
      })
      .catch((error) => {
        closeCard();
        window.alert(error);
      })
      .finally(() => setFavIsLoading(false));
  };
  return (
    <Card
      style={{
        width: "75 rem",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
      className="mt-5 pt-5"
    >
      <Card.Img src={characterData.characterImageThumb} variant="top" />
      <Card.Body>
        <Card.Title>{characterName}</Card.Title>
        <Card.Body>
          <ul>
            <li>
              <span>
                <b>Actor Name: </b> {characterData.actorName || "NA"}
              </span>
            </li>
            <li>
              <span>
                <b>NickName: </b> {characterData.nickname || "NA"}
              </span>
            </li>
            <li>
              <span>
                <b>Parents: </b> {characterData.parents || "NA"}
              </span>
            </li>
            <li>
              <span>
                <b>Siblings: </b> {characterData.siblings || "NA"}
              </span>
            </li>
            <li>
              <span>
                <b>Killed: </b> {characterData.killed || "NA"}
              </span>
            </li>
            <li>
              <span>
                <b>Killed By: </b> {characterData.killedBy || "NA"}
              </span>
            </li>
          </ul>
        </Card.Body>
        <Card.Footer>
          {isAdded ? (
            <div className="text-success">Added to favorites</div>
          ) : (
            <Button disabled={isLoading} onClick={() => addToFavorites(id)}>
              Add to Favorite
            </Button>
          )}{" "}
          <Button
            variant="danger"
            className="mr-5 pl-5"
            onClick={() => closeCard()}
          >
            Close
          </Button>
        </Card.Footer>
      </Card.Body>
    </Card>
  );
};
