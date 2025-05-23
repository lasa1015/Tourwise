import React, { useContext, useState, useEffect } from 'react';
import { CardMedia, Typography, IconButton, Box, Stack, Rating } from '@mui/material';
import { CloseRounded } from '@mui/icons-material';
import Btn_Add from '../Btn_Add';
import Tag_Category from '../Tag_Category';
import Tag_IsFree from '../Tag_IsFree';
import theme from '../../utils/theme';
import { ListContext } from '../../contexts/ListContext';
import { useResponsiveCardWidth } from '../../utils/useResponsiveSizes';
import { useAlertContext } from '../../contexts/AlertContext';

interface SpotCardPopUpProps {
  id: number;
  image1: string;
  image3: string;
  title: string;
  rating: number;
  isFree: boolean;
  category: string;
  user_ratings_total: number;
  onClose: () => void;
}

const SpotCard_PopUp: React.FC<SpotCardPopUpProps> = ({
  id, image1, image3, title, rating, category, isFree, user_ratings_total, onClose
}) => {
  const [currentImage, setCurrentImage] = useState(image1);
  const [imageStyle, setImageStyle] = useState({ transition: 'none', transform: 'scale(1)' });

  const { addItemWithDateCheck, isItemInList } = useContext(ListContext);
  const { openMissingDatesAlert } = useAlertContext();

  useEffect(() => {
    setCurrentImage(image1);
  }, [image1]);

  const handleAdd = () => {
    const spotData = { id, title, image: image1 };
    addItemWithDateCheck(spotData, openMissingDatesAlert, 'SpotCard_PopUp');
  };

  const isAdded = isItemInList(title);
  const cardWidth = useResponsiveCardWidth();

  return (
    <div style={{ borderRadius: '8px', overflow: 'hidden', width: cardWidth, background: 'white', marginBottom: 15, padding: 0 }}>
      <Box sx={{ position: 'relative', overflow: 'hidden', margin: 0, padding: 0 }}>
        <CardMedia
          component="img"
          height="200"
          image={currentImage}
          alt={title}
          sx={{ ...imageStyle, height: '170px', margin: 0, padding: 0 }}
          onMouseEnter={() => {
            setCurrentImage(image3);
            setImageStyle({ transition: 'transform 7s ease', transform: 'scale(1.4)' });
          }}
          onMouseLeave={() => {
            setCurrentImage(image1);
            setImageStyle({ transition: 'none', transform: 'scale(1)' });
          }}
        />
      </Box>

      <Stack sx={{ paddingTop: '7px', paddingLeft: '12px', margin: 0 }} gap="2px">
        <Typography sx={{ ...theme.typography.cardTitle, overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis', maxWidth: '95%', margin: 0, fontWeight: 400 }}>
          {title}
        </Typography>

        <Stack direction="row" gap={1} sx={{ margin: 0, alignItems: 'center' }}>
          <Rating name="half-rating-read" defaultValue={rating} precision={0.1} readOnly sx={{ fontSize: '1.2rem' }} />
          <span style={{ fontSize: '14px' }}>{rating}</span>
          <span style={{ color: '#999', fontSize: '13px', fontWeight: 400 }}>by {user_ratings_total} people</span>
        </Stack>

        <Stack direction="row" gap={1} sx={{ marginTop: '3px' }}>
          <Tag_Category category={category} />
          {isFree && <Tag_IsFree />}
        </Stack>

        <Stack direction="row" justifyContent="space-between" sx={{ width: '95%', paddingTop: 1.5, margin: 0 }}>
          <Btn_Add onClick={handleAdd} isAdded={isAdded} />
          <IconButton onClick={onClose}><CloseRounded /></IconButton>
        </Stack>
      </Stack>
    </div>
  );
};

export default SpotCard_PopUp;